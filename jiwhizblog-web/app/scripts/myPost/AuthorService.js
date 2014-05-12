/**
 * Service for author resource.
 * 
 */
angular.module('jiwhizWeb').factory('AuthorService', [ 'halClient', function(halClient) {
    
    return {
        'load' : 
            function() {
                return halClient.$get('api/author');
            }
    };
    
}]);