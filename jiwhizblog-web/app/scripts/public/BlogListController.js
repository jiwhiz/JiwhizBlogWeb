/**
 * List all public blog posts for blog-list.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('BlogListController',
['$rootScope', '$scope', '$timeout', 'WebsiteService', 
function($rootScope, $scope, $timeout, WebsiteService) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : 'active',
        'about' : '',
        'contact' : '',
        'admin' : ''
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'My Personal Blog';
    $rootScope.page_description = 'Some of my thoughts and experiences.';
    
    var setup = function( pageNumber ) {
        WebsiteService.load()
            .then( function( websiteResource ) {
                return websiteResource.$get('blogs', {'page': pageNumber, 'size':10, 'sort':null});
            })
            .then( function( resource )
            {
                $scope.page = resource.page;
                $scope.page.currentPage = $scope.page.number + 1;
                return resource.$get('blogPostList');
            })
            .then( function( blogPostList )
            {
                $scope.blogs = blogPostList;
                blogPostList.forEach( function( blog ) {
                    blog.contentFirstParagraph = getFirstSection(blog.content);

                    // load author profile
                    blog.$get('author').then(function(author) {
                        blog.author = author;
                    });
                    
                });
                
            })
            ;
        
    };
    
    setup(0);
    
    $scope.selectBlogPage = function(pageNumber) {
        setup(pageNumber-1); //Spring HATEOAS page starts with 0
    };
}
]);

