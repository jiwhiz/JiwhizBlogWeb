/**
 * Service for admin resource.
 * 
 */
'use strict';

angular.module('jiwhizWeb').factory('AdminService', [ 'halClient', function(halClient) {
    
    return {
        'load' : 
            function() {
                return halClient.$get('api/admin');
            }
    };
    
}]);