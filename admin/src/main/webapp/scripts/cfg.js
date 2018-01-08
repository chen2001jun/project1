require.config({
    baseUrl: 'scripts',
    paths: {
        'angular': 'components/angular/angular.min',
        'angular-animate': 'components/angular/angular-animate.min',
        'angular-cookies': 'components/angular/angular-cookies.min',
        'angular-resource': 'components/angular/angular-resource.min',
        'angular-ui-router': 'components/angular/angular-ui-router.min',
        'angular-async-loader': 'components/angular/angular-async-loader.min',

        'umeditor': 'plugins/umeditor/umeditor'
    },
    shim: {
        'angular': {exports: 'angular'},
        'angular-ui-router': {deps: ['angular']},
        'umeditor': {deps:['plugins/umeditor/umeditor.config'], exports: 'UM'}
    }
});

require(['angular', 'app-config'], function (angular) {
    angular.element(document).ready(function () {
        angular.bootstrap(document, ['app']);
        angular.element(document).find('html').addClass('ng-app');
    });
});
