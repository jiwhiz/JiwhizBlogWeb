/**
 * Display contact page. For contact.html.
 * 
 * @param $scope
 * @param $http
 */
function ContactController($scope, $http) {
    console.log('==>ContactController');
    $("#headmenu").find("li").removeClass("active");
    $("#contactmenu").addClass("active");

    $scope.contact = {};

    $scope.submit = function(contact) {
        console.log('click submit');
        $http.post('rest/public/contact', contact).
            success( function(data, status) {
                $scope.contact = {};
                $scope.message = 'Hi ' + contact.name
                        + ', your message was received. I will response as soon as possible. Thank you!';
            }).
            error( function(data, status) {
                $scope.contact = {};
                $scope.message = 'Error! status is ' + status;
            });
        $('#messageModel').modal('show');
    };

}