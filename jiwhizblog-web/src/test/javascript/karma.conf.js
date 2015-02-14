// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function(config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath : '../../',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks : [ 'jasmine' ],

        // list of files / patterns to load in the browser
        files : [
            // bower:js
            'main/webapp/bower_components/jquery/dist/jquery.js',
            'main/webapp/bower_components/es5-shim/es5-shim.js',
            'main/webapp/bower_components/angular/angular.js',
            'main/webapp/bower_components/angular-resource/angular-resource.js',
            'main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'main/webapp/bower_components/angular-route/angular-route.js',
            'main/webapp/bower_components/json3/lib/json3.js',
            'main/webapp/bower_components/bootstrap/dist/js/bootstrap.js',
            'main/webapp/bower_components/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js',
            'main/webapp/bower_components/components-underscore/underscore.js',
            'main/webapp/bower_components/highlightjs/highlight.pack.js',
            'main/webapp/bower_components/rfc6570/rfc6570.js',
            'main/webapp/bower_components/angular-hal/angular-hal.js',
            'main/webapp/bower_components/marked/lib/marked.js',
            'main/webapp/bower_components/angular-marked/angular-marked.js',
            'main/webapp/bower_components/angular-mocks/angular-mocks.js',
            // endbower
    
            'main/webapp/scripts/app.js',
            'main/webapp/scripts/**/*.js', 
            'test/javascript/spec/**/*.js'
        ],

        // list of files / patterns to exclude
        exclude : [],

        // web server port
        port : 8080,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel : config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch : false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers : [ 'PhantomJS' ],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun : false
    });
};
