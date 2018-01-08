define(['app'], function (app) {
    'use strict';

    app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function ($stateProvider, $urlRouterProvider, $httpProvider) {

        $httpProvider.interceptors.push(function ($q) {
            return {
                'responseError': function (rejection) {
                    if (rejection.status === 401) {
                        location.href = "login.html";
                    } else {
                        return $q.reject(rejection);
                    }
                }
            };
        });

        $urlRouterProvider.otherwise('/index');
        $stateProvider.state('index', {
            url: '/index',
            templateUrl: 'scripts/controller/views/index.html'
        }).state('articleDownload', {       // 文章
            url: '/articles/download',
            templateUrl: 'scripts/controller/views/articleDownload.html',
            controllerUrl: 'controller/article'
        }).state('articleList', {
            url: '/articles',
            templateUrl: 'scripts/controller/views/articleList.html',
            controllerUrl: 'controller/article'
        }).state('articleDetail', {
            url: '/articles/:id',
            templateUrl: 'scripts/controller/views/articleDetail.html',
            controllerUrl: 'controller/article'
        }).state('docList', {               // 文库
            url: '/doc',
            templateUrl: 'scripts/controller/views/docList.html',
            controllerUrl: 'controller/doc'
        }).state('docDetail', {
            url: '/doc/:id',
            templateUrl: 'scripts/controller/views/docDetail.html',
            controllerUrl: 'controller/doc'
        }).state('docTagList', {
            url: '/doctag',
            templateUrl: 'scripts/controller/views/docTagList.html',
            controllerUrl: 'controller/docTag'
        }).state('docTagDetail', {
            url: '/doctag/:id',
            templateUrl: 'scripts/controller/views/docTagDetail.html',
            controllerUrl: 'controller/docTag'
        }).state('docCategory', {
            url: '/docCategory',
            templateUrl: 'scripts/controller/views/docCategory.html',
            controllerUrl: 'controller/docCategory'
        }).state('userList', {              // 用户
            url: '/user',
            templateUrl: 'scripts/controller/views/userList.html',
            controllerUrl: 'controller/user'
        }).state('userDetail', {
            url: '/user/:id',
            templateUrl: 'scripts/controller/views/userDetail.html',
            controllerUrl: 'controller/user'
        }).state('wxqaList', {              // 微信
            url: '/wxqa',
            templateUrl: 'scripts/controller/views/wxqaList.html',
            controllerUrl: 'controller/wxQa'
        }).state('wxqaDetail', {
            url: '/wxqa/:id',
            templateUrl: 'scripts/controller/views/wxqaDetail.html',
            controllerUrl: 'controller/wxQa'
        }).state('wxHelpage', {
            url: '/wxhelpage',
            templateUrl: 'scripts/controller/views/wxHelpage.html',
            controllerUrl: 'controller/wxHelpage'
        }).state('wxMenu', {
            url: '/wxmenu',
            templateUrl: 'scripts/controller/views/wxMenu.html',
            controllerUrl: 'controller/wxMenu'
        }).state('wxArticle', {
            url: '/wxarticle',
            templateUrl: 'scripts/controller/views/wxArticle.html',
            controllerUrl: 'controller/wxArticle'
        }).state('searchWord', {              // 报表
            url: '/searchword',
            templateUrl: 'scripts/controller/views/searchWordList.html',
            controllerUrl: 'controller/searchWord'
        }).state('docReport', {
            url: '/docreport/:beginTime/:endTime',
            templateUrl: 'scripts/controller/views/reportDocList.html',
            controllerUrl: 'controller/docReport'
        }).state('monthReport', {
            url: '/monthreport',
            templateUrl: 'scripts/controller/views/monthReportList.html',
            controllerUrl: 'controller/monthReport'
        }).state('userReport', {
            url: '/userreport',
            templateUrl: 'scripts/controller/views/reportUserList.html',
            controllerUrl: 'controller/userReport'
        });
    }]).run(['$rootScope', '$http', '$state', '$stateParams', function ($rootScope, $http, $state, $stateParams) {
        $http.get('i/config').success(function (config) {
            angular.extend($rootScope, config);
        });
        $http.get('admin/login').success(function (admin) {
            $rootScope.admin = admin;
            $rootScope.admin.isRoot = (admin.account === 'cncadmin');
        });

        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;

        $rootScope.logout = function () {
            $http.get('admin/logout').success(function () {
                location.href = "login.html";
            }).error(function () {
                location.href = "login.html";
            })
        }
    }]);
});