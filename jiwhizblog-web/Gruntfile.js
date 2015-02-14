'use strict';

var pomParser = require('node-pom-parser');
var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;


// usemin custom step. see https://github.com/yeoman/grunt-usemin#user-defined-steps-and-post-processors
var useminAutoprefixer = {
    name: 'autoprefixer',
    createConfig: require('grunt-usemin/lib/config/cssmin').createConfig // Reuse cssmins createConfig
};

module.exports = function(grunt) {

    // Automatically load all grunt tasks matching the ['grunt-*', '@*/grunt-*']
    // patterns
    require('load-grunt-tasks')(grunt);

    // Time how long tasks take. Can help when optimizing build times
    require('time-grunt')(grunt);

    // Define the configuration for all the tasks
    grunt.initConfig({
    
        // Project settings
        yeoman : {
            // configurable paths
            app : 'src/main/webapp',
            test : 'src/test/javascript/spec',
            dist : 'src/main/webapp/dist'
        },

        // https://github.com/gruntjs/grunt-contrib-less
        //   compile LESS to CSS
        less : {
            dist: {
                options : {
                    paths : [ "<%= yeoman.app %>/styles" ]
                },
                files : {
                    "<%= yeoman.app %>/styles/jiwhizblog-website.css" : "<%= yeoman.app %>/styles/jiwhizblog-website.less"
                }
            }
        },

        // Watches files for changes and runs tasks based on the changed
        // files
        watch : {
            bower: {
                files: ['bower.json'],
                tasks: ['wiredep']
            },
            ngconstant: {
                files: ['Gruntfile.js', 'pom.xml'],
                tasks: ['ngconstant:dev']
            },
            js : {
                files : [ '<%= yeoman.app %>/scripts/{,*/}*.js' ],
                tasks : [ 'newer:jshint:all' ],
                options : {
                    livereload : true
                }
            },
            jsTest : {
                files : [ '<%= yeoman.test %>/{,*/}*.js' ],
                tasks : [ 'newer:jshint:test', 'karma' ]
            },
            styles : {
                files : [ '<%= yeoman.app %>/styles/{,*/}*.css' ],
                tasks : [ 'newer:copy:styles', 'autoprefixer' ]
            },
            gruntfile : {
                files : [ 'Gruntfile.js' ]
            },
            livereload : {
                options : {
                    livereload : '<%= connect.options.livereload %>'
                },
                files : [ 
                    'src/main/webapp/**/*.html',
                    'src/main/webapp/**/*.json',
                    '{.tmp/,}src/main/webapp/assets/styles/**/*.css',
                    '{.tmp/,}src/main/webapp/scripts/**/*.js',
                    'src/main/webapp/assets/images/**/*.{png,jpg,jpeg,gif,webp,svg}'
                ]
            }
        },
        
        // https://github.com/stephenplusplus/grunt-wiredep
        //   Inject Bower package into your source code.
        wiredep: {
            app: {
                src: ['src/main/webapp/index.html'],
                exclude: [/angular-i18n/, /swagger-ui/]
            },
            test: {
                src: 'src/test/javascript/karma.conf.js',
                exclude: [/angular-i18n/, /swagger-ui/, /angular-scenario/],
                ignorePath: /\.\.\/\.\.\//, // remove ../../ from paths of injected javascripts
                devDependencies: true,
                fileTypes: {
                    js: {
                        block: /(([\s\t]*)\/\/\s*bower:*(\S*))(\n|\r|.)*?(\/\/\s*endbower)/gi,
                        detect: {
                            js: /'(.*\.js)'/gi
                        },
                        replace: {
                            js: '\'{{filePath}}\','
                        }
                    }
                }
            }
        },
    
        // The actual grunt server settings
        
        // https://github.com/gruntjs/grunt-contrib-connect
        //   Start a connect web server
        // https://github.com/drewzboto/grunt-connect-proxy
        //   Provides a http proxy as middleware for the grunt-contrib-connect plugin
        connect : {
            options : {
                port : 9000,
                // Change this to '0.0.0.0' to access the server from
                // outside.
                hostname : 'localhost',
                livereload : 35729
            },
            livereload : {
                options : {
                    open : true,
                    base : [ '.tmp', '<%= yeoman.app %>' ]
                }
            },
            test : {
                options : {
                    port : 9001,
                    base : [ '.tmp', 'test', '<%= yeoman.app %>' ]
                }
            },
            dist : {
                options : {
                    base : '<%= yeoman.dist %>'
                }
            }
        },
    
        // Make sure code styles are up to par and there are no obvious
        // mistakes
        jshint : {
            options : {
                jshintrc : '.jshintrc',
                reporter : require('jshint-stylish')
            },
            all : [ 'Gruntfile.js', '<%= yeoman.app %>/scripts/**/*.js' ],
            test : {
                options : {
                    jshintrc : '.jshintrc'
                },
                src : [ 'test/spec/**/*.js' ]
            }
        },
    
        // https://github.com/gruntjs/grunt-contrib-clean
        // Empties folders to start fresh
        clean : {
            dist : {
                files : [ {
                    dot : true,
                    src : [ 
                        '.tmp',
                        '<%= yeoman.dist %>/*', 
                        '!<%= yeoman.dist %>/.git*',
                        '<%= yeoman.app %>/styles/jiwhizblog-website.css', 
                    ]
                } ]
            },
            server : '.tmp'
        },

        // https://github.com/sindresorhus/grunt-concurrent
        // Run some tasks in parallel to speed up the build process
        concurrent : {
            server : [ ],
            test : [ ],
            dist : [  'imagemin', 'svgmin' ]
        },

        
        // https://github.com/gruntjs/grunt-contrib-concat
        //  useminPrepare generated task will do the concat for css and js files.
        concat: {
        },
        
        // https://github.com/nDmitry/grunt-autoprefixer
        // Parse CSS and add vendor-prefixed CSS properties using the Can I Use database
        autoprefixer : {
        },
    
        // https://github.com/yeoman/grunt-usemin
        // Reads HTML for usemin blocks to enable smart builds that automatically
        // concat, minify and revision files. Creates configurations in
        // memory so additional tasks can operate on them
        useminPrepare : {
            html : '<%= yeoman.app %>/index.html',
            options : {
                dest : '<%= yeoman.dist %>',
                flow: {
                    html: {
                        steps: {
                            js: ['concat', 'uglifyjs'],
                            css: ['cssmin', useminAutoprefixer] // Let cssmin concat files so it corrects relative paths to fonts and images
                        },
                        post: {}
                    }
                }
            }
        },
    
        // Performs rewrites based on rev and the useminPrepare
        // configuration
        usemin : {
            html : [ '<%= yeoman.dist %>/**/*.html' ],
            css : [ '<%= yeoman.dist %>/styles/*.css' ],
            js: ['<%= yeoman.dist %>/scripts/*.js'],
            options : {
                assetsDirs : [ '<%= yeoman.dist %>' ],
                dirs: ['<%= yeoman.dist %>'],
                // This is so we update image references in our ng-templates and css files
                patterns: {
                  js: [
                    [/(images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the JS to reference our revved images']
                  ],
                  css: [
                    [/(images\/.*?\.(?:gif|jpeg|jpg|png|webp|svg))/gm, 'Update the CSS to reference our revved images']
                  ]
                }
            }
        },

        // https://github.com/ericclemmons/grunt-angular-templates
        // Speed up your AngularJS app by automatically minifying, combining, and automatically caching your HTML templates with $templateCache
        ngtemplates:    {
            dist: {
                cwd: '<%= yeoman.app %>',
                src: ['views/**/*.html'],
                dest: '.tmp/templates/templates.js',
                options: {
                    module: 'jiwhizWeb',
                    usemin: 'scripts/app.js',
                    htmlmin:  {
                        removeCommentsFromCDATA: true,
                        // https://github.com/yeoman/grunt-usemin/issues/44
                        collapseWhitespace: true,
                        collapseBooleanAttributes: true,
                        conservativeCollapse: true,
                        removeAttributeQuotes: true,
                        removeRedundantAttributes: true,
                        useShortDoctype: true,
                        removeEmptyAttributes: true
                    }
                }
            }
        },
        
        // The following *-min tasks produce minified files in the dist folder
        
        // https://github.com/gruntjs/grunt-contrib-imagemin
        imagemin : {
            dist : {
                files : [ {
                    expand : true,
                    cwd : '<%= yeoman.app %>/images',
                    src : '**/*.{jpg,jpeg,gif}',
                    dest : '<%= yeoman.dist %>/images'
                } ]
            }
        },
        // https://github.com/sindresorhus/grunt-svgmin
        svgmin : {
            dist : {
                files : [ {
                    expand : true,
                    cwd : '<%= yeoman.app %>/images',
                    src : '**/*.svg',
                    dest : '<%= yeoman.dist %>/images'
                } ]
            }
        },
        // https://github.com/gruntjs/grunt-contrib-cssmin
        //  useminPrepare generated cssmin task will do the job.
        cssmin: {
        },
        
        // https://github.com/gruntjs/grunt-contrib-htmlmin
        // Minify HTML
        htmlmin : {
            dist : {
                options : {
                    removeCommentsFromCDATA: true,
                    // https://github.com/yeoman/grunt-usemin/issues/44
                    collapseWhitespace: true,
                    collapseBooleanAttributes: true,
                    conservativeCollapse: true,
                    removeAttributeQuotes: true,
                    removeRedundantAttributes: true,
                    useShortDoctype: true,
                    removeEmptyAttributes: true
                },
                files : [ {
                    expand : true,
                    cwd : '<%= yeoman.dist %>',
                    src : [ '*.html', 'views/**/*.html' ],
                    dest : '<%= yeoman.dist %>'
                } ]
            }
        },

        // https://github.com/gruntjs/grunt-contrib-copy
        // Copies remaining files to places other tasks can use
        copy : {
            dist : {
                files : [{
                    expand : true,
                    dot : true,
                    cwd : '<%= yeoman.app %>',
                    dest : '<%= yeoman.dist %>',
                    src : [ 
                        '*.html', 
                        'views/**/*.html',
                        'images/**/*.{png,webp}', 
                        'fonts/*' 
                    ]
                }, {
                    expand : true,
                    cwd : '.tmp/images',
                    dest : '<%= yeoman.dist %>/images',
                    src : [ 'generated/*' ]  //TODO ? who will generate image files?
                } ]
            },
        },
    
        // https://github.com/mzgol/grunt-ng-annotate
        // Add, remove and rebuild AngularJS dependency injection annotations. base on ng-annotate
        ngAnnotate: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '.tmp/concat/scripts', 
                    src: '*.js', 
                    dest: '.tmp/concat/scripts'
                }]
            }
        },
        
        // https://github.com/gruntjs/grunt-contrib-uglify
        // Minify files with UglifyJS
        //   not used, since useminPrepare generated uglify task will do the job
        uglify: {
        },

        // https://github.com/yoniholmes/grunt-text-replace
        //   Replace text in files using strings, regexs or functions.
        // TODO find usage???
        replace: {
            dist: {
                src: ['<%= yeoman.dist %>/index.html'],
                overwrite: true,  // overwrite matched source files
                replacements: [{
                    from: '<div class="development"></div>',
                    to: ''
                }]
            }
        },
        
        // https://github.com/yeoman/grunt-filerev
        //  Filerev
        filerev: {
            options: {
                encoding: 'utf8',
                algorithm: 'md5',
                length: 20
            },
            dist: {
                // hashes(md5) all assets (images, js and css )
                // in dist directory
                files: [{
                    src: [
                        '<%= yeoman.dist %>/images/*.{png,gif,jpg,svg}',
                        '<%= yeoman.dist %>/scripts/*.js',
                        '<%= yeoman.dist %>/styles/*.css',
                    ]
                }]
            }
        },
        
        // https://github.com/karma-runner/grunt-karma
        // Test settings
        karma : {
            unit : {
                configFile : 'src/test/javascript/karma.conf.js',
                singleRun : true
            }
        },

    });

    grunt.registerTask('serve', function(target) {
        if (target === 'dist') {
            return grunt.task.run([ 'build', 'connect:dist:keepalive' ]);
        }

        grunt.task.run([ 
            'clean:server',
            'bower-install',
            'concurrent:server',
            'autoprefixer',
            "less:dev",
            'connect:livereload',
            'watch'
        ]);
    });

    grunt.registerTask('server', function(target) {
        grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
        grunt.task.run([target ? ('serve:' + target) : 'serve']);
    });

    grunt.registerTask('test', [ 
        'clean:server', 
        'wiredep:test',
        'concurrent:test', 
        'connect:test', 
        'karma' ]);

    grunt.registerTask('build', [ 
        'clean:dist',
        'wiredep:app',
        'less',
        'useminPrepare',
        'ngtemplates', 
        'concurrent:dist',
        'concat',
        'copy:dist',
        'ngAnnotate',
        'cssmin',
        'autoprefixer',
        'uglify',
        'filerev',
        'usemin',
        'htmlmin' // because of the issue: https://github.com/yeoman/grunt-usemin/issues/44, put htmlmin after usemin
    ]);

    grunt.registerTask('default', [
        'test',
        'build'
    ]);
};
