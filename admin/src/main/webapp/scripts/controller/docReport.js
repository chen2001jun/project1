define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller("reportDocListCtrl",['$scope', '$rootScope', '$http', '$state', '$stateParams', function ($scope, $rootScope, $http, $state, $stateParams){
        if (!$rootScope.reportDocParams) {
            $rootScope.reportDocParams = {};
        }
        $scope.searchKey = $rootScope.reportDocParams.content || '';
        $scope.beginTime = $stateParams.beginTime || $rootScope.reportDocParams.beginTime || '';
        $scope.endTime = $stateParams.endTime || $rootScope.reportDocParams.endTime || '';
        $scope.params = {
            content: $rootScope.reportDocParams.content || null,
            beginTime:$stateParams.beginTime || $rootScope.reportDocParams.beginTime || '',
            endTime:$stateParams.endTime || $rootScope.reportDocParams.endTime || '',
            page: $rootScope.reportDocParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/docreport', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.reportDocParams.page = $scope.params.page;
                $rootScope.reportDocParams.beginTime = $scope.params.beginTime;
                $rootScope.reportDocParams.endTime = $scope.params.endTime;
            });
            $http.get('admin/docreport/downloads', {params: $scope.params}).success(function (data) {
                $scope.totalDownloads = data;
            })
        };
        $scope.onSearchKeyPress = function (e) {
            var keycode = window.event ? e.keyCode : e.which;
            if (keycode == 13) {
                $scope.doSearch();
            }
        };
        $scope.doSearch = function () {
            delete $scope.data;
            $scope.params.content = $scope.searchKey;
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;
            $rootScope.reportDocParams.content = $scope.searchKey;
            $rootScope.reportDocParams.beginTime = $scope.beginTime;
            $rootScope.reportDocParams.endTime = $scope.endTime;
            $scope.params.page = 1;
            $scope.getData();
        };

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };

    }])
});