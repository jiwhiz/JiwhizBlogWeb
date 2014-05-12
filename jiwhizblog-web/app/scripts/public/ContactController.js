/**
 * Display contact page. For contact.html.
 * 
 */
'use strict';

angular.module('jiwhizWeb').controller('ContactController',
['$rootScope', '$scope', '$http', '$modal',
function($rootScope, $scope, $http, $modal) {
    $rootScope.activeMenu = {
        'home' : '',
        'blog' : '',
        'about' : '',
        'contact' : 'active',
        'admin' : ''
    };
    $rootScope.showTitle = true;
    
    $rootScope.page_title = 'Contact Me';
    $rootScope.page_description = 'We should get together and talk!';

    $scope.contact = {};

    $scope.submit = function(contact) {
        $http.post('api/public/contact', contact);
        
        $modal.open({
            templateUrl: 'views/public/contact-confirmation.html',
            controller: 'ContactConfirmationDialogController',
            resolve: {
                name: function () {
                    return $scope.contact.name;
                }
            }
        });

        $scope.contact = {};
    };

}
]);
