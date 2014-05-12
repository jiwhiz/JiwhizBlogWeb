/**
 * Setup side bar block data.
 *  
 */
'use strict';

angular.module('jiwhizWeb').controller('SidebarController',
['$scope', '$q', 'WebsiteService', 
function($scope, $q, WebsiteService) {
    $scope.recentBlogs = {};
    $scope.recentComments = [];
    $scope.tagCloud = {};
    
    WebsiteService.load()
        .then( function( websiteResource ) {
            return websiteResource.$get('recentBlogs');
        })
        .then( function(resource) {
            if (resource.$has('blogPostList')) {
                return resource.$get('blogPostList');
            }
            return $q.reject("no recent blogs!");
        })
        .then( function(blogPostList){
            $scope.recentBlogs = blogPostList;
        });
    
    WebsiteService.load()
        .then( function( websiteResource ) {
            return websiteResource.$get('recentComments');
        })
        .then( function(resource) {
            if (resource.$has('commentPostList')) {
                return resource.$get('commentPostList');
            }
            return $q.reject("no recent comments!");
        })
        .then( function(commentPostList){
            $scope.recentComments = commentPostList;
            angular.forEach($scope.recentComments, function(comment){
                comment.$get('author').then( function(author){
                    comment.author = author;
                });
                comment.$get('blog').then( function(blog){
                    comment.blog = blog;
                });
            });
        });

    WebsiteService.load()
        .then( function( websiteResource ) {
            return websiteResource.$get('tagCloud');
        })
        .then( function(tagClouds) {
            processTagClouds(tagClouds);
        });
    
    var opts = {
        size: {start:6, end: 20, unit: 'pt'},
        color: {start: '##115700', end: '#006600'}
    };
    
    // Converts hex to an RGB array
    var toRGB = function(code) {
      if (code.length === 4) {
        code = code.replace(/(\w)(\w)(\w)/gi, "\$1\$1\$2\$2\$3\$3");
      }
      var hex = /(\w{2})(\w{2})(\w{2})/.exec(code);
      return [parseInt(hex[1], 16), parseInt(hex[2], 16), parseInt(hex[3], 16)];
    };

    // Converts an RGB array to hex
    var toHex = function(ary) {
      return "#" + jQuery.map(ary, function(i) {
        var hex =  i.toString(16);
        hex = (hex.length === 1) ? "0" + hex : hex;
        return hex;
      }).join("");
    };

    var colorIncrement = function(color, range) {
      return jQuery.map(toRGB(color.end), function(n, i) {
        return (n - toRGB(color.start)[i])/range;
      });
    };

    var tagColor = function(color, increment, weighting) {
      var rgb = jQuery.map(toRGB(color.start), function(n, i) {
        var ref = Math.round(n + (increment[i] * weighting));
        if (ref > 255) {
          ref = 255;
        } else {
          if (ref < 0) {
            ref = 0;
          }
        }
        return ref;
      });
      return toHex(rgb);
    };

    function processTagClouds(tagClouds) {
        var highest = -1;
        var lowest = 999999;
        tagClouds.forEach( function(tagCloud) {
            if (tagCloud.count > highest) {
                highest = tagCloud.count;
            }
            if (tagCloud.count < lowest) {
                lowest = tagCloud.count;
            }

        });
        
        var range = highest - lowest;
        if(range === 0) {range = 1;}
        var fontIncr = (opts.size.end - opts.size.start)/range;
        var colorIncr = colorIncrement (opts.color, range);

        tagClouds.forEach( function(tagCloud) {
            var weighting = tagCloud.count - lowest;
            tagCloud.fontSize = opts.size.start + (weighting * fontIncr) + opts.size.unit;
            tagCloud.color = tagColor(opts.color, colorIncr, weighting);
            //tagCloud.tag = tagCloud.tag+' '; //add a space
        });
        
        $scope.tagClouds = tagClouds;
    }
}

]);