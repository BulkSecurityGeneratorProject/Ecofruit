(function() {
    'use strict';

    angular
        .module('ecofruitApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'ngMap',
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler', '$location','$rootScope'];

    function run(stateHandler, translationHandler, $location, $rootScope ) {
        stateHandler.initialize();
        translationHandler.initialize();
        $rootScope.path = {
            get: function(){
                return $location.path();
            }
        }

    }
})();
