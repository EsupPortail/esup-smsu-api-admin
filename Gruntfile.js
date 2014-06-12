module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
      pkg: grunt.file.readJSON('package.json'),

      ngtemplates:  {
	  myApp:        {
	      src:      'src/main/webapp/partials/**.html',
	      dest:     'src/main/webapp/partials/all.js',
	      options:      {
		  bootstrap:  function(module, script) {
		      script = script.replace(/templateCache.put\('webapp\/(.*?)'/g, "templateCache.put(document.esupSmsuApiAdmin.baseURL + '/$1'");
		      return "angular.module('myApp').run(['$templateCache', function($templateCache) { \n" + script + "\n}]);"
		  },
		  htmlmin: { collapseWhitespace: true, removeComments: true }
	      }
	  }
      },
      jquery: {
	  dist: {
	      output: "src/main/webapp/js",
	      options: {
		  prefix: "jquery-",
		  //minify: false
	      },
	      versions: { "1.10.2": "ajax,deprecated" }
	  }
      },
      bower: {
	  download: {
	      options: { verbose: true, copy: false }
	  }
      },

      concat:   {
	  options: { separator: ";\n\n;\n" },
	  myApp:    {
	      src:  [ 'src/main/webapp/js/jquery*.js', 
		      'bower_components/angular/angular.min.js',
		      'bower_components/angular-i18n/angular-locale_fr-fr.js',
		      'bower_components/ng-grid/ng-grid-*.min.js',
		      'src/main/webapp/js/app.js', '<%= ngtemplates.myApp.dest %>' ],
	      dest: 'src/main/webapp/js/all.js'
	  }
      },

      watch: {
	  scripts: {
	      files: ['**/*.js'],
	      tasks: ['ngtemplates','concat'],
	      options: { spawn: false },
	  },
      },

      connect: {
	  server: {
	      options: {}
	  }
      },
      open : {
	  dev : {
	      path: 'http://127.0.0.1:8000/src/main/webapp/test/StartPage.html',
	      app: 'x-www-browser',
	  }
      }
  });

  grunt.registerTask('and-concat', ['ngtemplates', 'jquery', 'bower', 'concat']);

  grunt.loadNpmTasks('grunt-angular-templates');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks("grunt-jquery-builder");
  grunt.loadNpmTasks('grunt-bower-task');

  grunt.loadNpmTasks('grunt-contrib-connect');
  grunt.loadNpmTasks('grunt-open');
  grunt.loadNpmTasks('grunt-contrib-watch');
};
