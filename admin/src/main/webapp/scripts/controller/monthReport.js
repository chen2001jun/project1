define(['app', 'directive/ng-pager', 'directive/ng-file', 'filter/filesize'], function (app) {
    'use strict';

    app.controller("monthRecordListCtrl",['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http){
        if (!$rootScope.monthReportParams) {
            $rootScope.monthReportParams = {};
        }

        $scope.beginTime = $rootScope.monthReportParams.beginTime || '';
        $scope.endTime =  $rootScope.monthReportParams.endTime || '';

        $scope.params = {
            beginTime: $rootScope.monthReportParams.beginTime || null,
            endTime: $rootScope.monthReportParams.endTime || null,
            page: $rootScope.monthReportParams.page || 1,
            size: 20
        };

        // 获取数据
        $scope.getData = function () {
            $http.get('admin/monthreport', {params: $scope.params}).success(function (data) {


                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.monthReportParams.page = $scope.params.page;

            })


            $http.get('admin/monthreport/totalSum', {params: $scope.params}).success(function (data) {

                // alert($scope.articleViewsSum);
                // alert($scope.docViewsSum);
                // alert($scope.articleDownloadsSum);
                // alert($scope.docDownloadsSum);

                $scope.articleViewsSum=getTotalArticleViews(data);
                $scope.docViewsSum=getTotalDocViews(data);
                $scope.articleDownloadsSum=getTotalArticleDownloads(data);
                $scope.docDownloadsSum=getTotalDocDownloads(data);


            })

        };

        $scope.doSearch = function () {
            // alert("sadas");
            $scope.params.beginTime = $scope.beginTime;
            $scope.params.endTime = $scope.endTime;

            $rootScope.monthReportParams.beginTime = $scope.beginTime;
            $rootScope.monthReportParams.endTime = $scope.endTime;

            $scope.params.page = 1;

            //alert($scope.params.content);
            //alert($rootScope.monthReportParams.content);


            $scope.getData();
        }

        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };


        //计算资料浏览总量
        var getTotalArticleViews = function(data) {
            var sum = 0;
            var docs = data.content;
            sum=docs[0]['articleViews'];

            // for (var item in docs) {
            //     sum = docs[item]['articleViews'];
            //
            // }

            return sum;
        }


        //计算文库浏览总量
        var getTotalDocViews = function(data) {
            var sum = 0;
            var docs = data.content;
            //alert(docs[0]['docViews']);
            sum=docs[0]['docViews'];
            // for (var item in docs) {
            //     //sum += docs[item]['docViews'];
            //     sum=docs[item]['docViews'];
            //
            // }

            return sum;
        }


        //计算资料下载总量
        var getTotalArticleDownloads = function(data) {
            var sum = 0;
            var docs = data.content;
            sum=docs[0]['articleDownloads'];

            // for (var item in docs) {
            //     sum = docs[item]['articleDownloads'];
            //
            // }

            return sum;
        }


        //计算文库下载总量
        var getTotalDocDownloads = function(data) {
            var sum = 0;
            var docs = data.content;
            sum=docs[0]['docDownloads'];

            // for (var item in docs) {
            //     sum = docs[item]['docDownloads'];
            //
            //
            // }

            return sum;
        }

    }])
});