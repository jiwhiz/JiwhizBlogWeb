/**
 * Hypermedia-driven RESTful API service entry point for Website resources.
 * 
 */
'use strict';

angular.module('jiwhizWeb').factory('WebsiteService', [ 'halClient', function(halClient) {
    
    return {
        'load' : 
            function() { 
                return halClient.$get('api/public'); 
            },
    };
    
}]);