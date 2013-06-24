angular.module('jiwhizblog').
    directive('memberInfo', function() {
        return {
            restrict: 'EA',
            template:
                '<table class="member-info">' +
                '    <tr><td><span class="member-avatar">' +
                '        <img src="{{member.imageUrl}}" class="dashboard-member-activity-avatar" />' +
                '    </span></td></tr>' +
                '    <tr><td>UserId : <strong><a href="#/profile/{{member.userId}}">{{member.userId}}</a></strong></td></tr>' +
                '    <tr><td>Name : <strong>{{member.displayName}}</strong></td></tr>' +
                '    <tr ng-show="member.email"><td>Email : {{member.email}}</td></tr>' +
                '    <tr><td>WebSite : <a href="{{member.webSite | ExternalURL}}">{{member.webSite}}</a></td></tr>' +
                '</table>',
            scope: {
                member: '='
            },
            replace: true,
            transclude: false,
            link: function(scope, element, attrs) {
                console.log('==>memberInfo link function, member='+scope.member);
	        }
    };
});