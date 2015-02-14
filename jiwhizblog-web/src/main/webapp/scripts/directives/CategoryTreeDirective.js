'use strict';

angular.module('jiwhizWeb').directive('categoryTree',
['$timeout', 
function($timeout) {
    return {
        restrict : 'E',
        templateUrl : 'views/templates/category-tree-template.html',
        scope : {
            process : '=',
            onSelectCategory : '&',
            onSelectActivity : '&'
        },
        link : function(scope, element, attrs) {
            var on_treeData_change;
            if (!scope.process) {
                alert('no process defined for the category tree!');
                return;
            }
            if (attrs.forActivity == null) {
                attrs.forActivity = false;
            }
            console.log("for activity "+ attrs.forActivity);

            /*
             * Handle Category selection.
             */
            var selected_category = null;
            scope.clickCategory = function(category) {
                if (attrs.forActivity) {
                    return; //ignore if for activity
                }
                console.log("==>user_clicks_category, "+category.name);
                if (category !== selected_category) {
                    if (selected_category != null) {
                        selected_category.selected = false;
                    }
                    category.selected = true;
                    selected_category = category;
                    if (category.onSelect != null) {
                        return $timeout(function() {
                            return category.onSelect(category);
                        });
                    } else {
                        if (scope.onSelectCategory != null) {
                            return $timeout(function() {
                                return scope.onSelectCategory({
                                    category : category
                                });
                            });
                        }
                    }
                }
            };

            /*
             * Handle Activity selection.
             */
            var selected_activity = null;
            scope.clickActivity = function(activity) {
                if (!attrs.forActivity) {
                    return; //ignore if not for activity
                }
                console.log("==>user clicks activity: "+activity.name);
                if (activity !== selected_activity) {
                    if (selected_activity != null) {
                        selected_activity.selected = false;
                        console.log("unselect activity: "+selected_activity.name);
                    }
                    activity.selected = true;
                    selected_activity = activity;
                    if (activity.onSelect != null) {
                        return $timeout(function() {
                            return activity.onSelect(activity);
                        });
                    } else {
                        if (scope.onSelectActivity != null) {
                            return $timeout(function() {
                                return scope.onSelectActivity({
                                    activity : activity
                                });
                            });
                        }
                    }
                }
            };

            /*
             * Set up tree node list
             */
            scope.tree_rows = [];
            on_treeData_change = function() {
                
                /*
                 * walk through sub category tree and return array of tree nodes
                 */
                function getSubCatgoryTreeNodes(level, category, visible) {
                    var treeNodes = new Array();
                    var activityNodes = new Array();
                    var subCategoryNodes = new Array();
                    
                    //create activity nodes and check selection
                    category.selectedBranch = false;
                    if (attrs.forActivity && category.activities) {
                        category.activities.forEach( function(activity) {
                            if (activity.selected === true) {
                                category.selectedBranch = true;
                                category.expanded = true;
                                selected_activity = activity;
                            }
                            activityNodes.push({
                                level : level+1,
                                category : null,
                                activity : activity,
                                label : activity.name,
                                tree_icon : 'fa fa-truck',
                                visible : visible && category.expanded
                            });
                        });
                    }

                    if (category.expanded == null) {
                        category.expanded = false;
                    }

                    //create sub category nodes and check selection
                    if (category.subCategories) {
                        category.subCategories.forEach( function(subCategory) {
                            var subNodes = getSubCatgoryTreeNodes(level + 1, subCategory, visible && category.expanded);
                            if (subCategory.selectedBranch === true){
                                category.selectedBranch = true;
                                category.expanded = true;
                            }
                            
                            subNodes.forEach(function(subNode) {
                                subCategoryNodes.push(subNode);
                            });
                        });
                    }

                    //push category node to return array
                    treeNodes.push({
                        level : level,
                        category : category,
                        label : category.name,
                        tree_icon : (category.expanded) ? 'fa fa-folder-open-o' : 'fa fa-folder-o',
                        visible : visible,
                        activity : null
                    });

                    if (category.subCategories) {
                        //push sub category nodes to return array
                        subCategoryNodes.forEach(function(subCategoryNode) {
                            treeNodes.push(subCategoryNode);
                        });
                    }
                    
                    if (attrs.forActivity && category.activities) {
                        //push activity nodes
                        activityNodes.forEach( function(activityNode) {
                            treeNodes.push(activityNode);
                        });
                    }
                    
                    return treeNodes;
                };
                
                scope.tree_rows = [];
                if (scope.process.categories){
                    scope.process.categories.forEach( function(category) {
                        category.expanded = true; //expand first level tree
                        var categoryTreeNodes = getSubCatgoryTreeNodes(1, category, true);
                        categoryTreeNodes.forEach( function(node) {
                            scope.tree_rows.push(node);
                        });
                    });
                }
            };
            return scope.$watch('process', on_treeData_change, true);
        }
    };
}
]);