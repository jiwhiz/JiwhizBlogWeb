/**
 * Service for current user account.
 * 
 */
angular.module('jiwhizWeb').factory('MyAccountService', [ 'halClient', function(halClient) {
    
    return {
        'load' : 
            function() {
                return halClient.$get("api/user");
            }
    };
    
}]);