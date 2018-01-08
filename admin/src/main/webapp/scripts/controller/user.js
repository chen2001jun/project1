define(['app', 'directive/ng-pager', 'directive/ng-file'], function (app) {
    'use strict';

    var userStates = [];
    userStates[-1] = '删除';
    userStates[1] = '正常';

    app.controller('userListCtrl', ['$scope', '$rootScope', '$http', function ($scope, $rootScope, $http) {
        if (!$rootScope.userParams) {
            $rootScope.userParams = {};
        }
        $scope.states = userStates;
        $scope.searchKey = $rootScope.userParams.key || '';
        $scope.params = {
            key: $rootScope.userParams.key || null,
            sortBy: $rootScope.userParams.sortBy || null,
            sortType: $rootScope.userParams.sortType || null,
            page: $rootScope.userParams.page || 1,
            size: 20
        };
        // 获取数据
        $scope.getData = function () {
            $http.get('admin/user', {params: $scope.params}).success(function (data) {
                $scope.data = data;
                $scope.params.page = data.number + 1;
                $rootScope.userParams.page = $scope.params.page;
                $rootScope.userParams.sortBy = $scope.params.sortBy;
                $rootScope.userParams.sortType = $scope.params.sortType;
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
            $scope.params.key = $scope.searchKey;
            $rootScope.userParams.key = $scope.searchKey;
            $scope.params.page = 1;
            $scope.getData();
        };
        // 删除
        $scope.delete = function (a) {
            if (confirm('确定要删除【' + a.name + '】吗？')) {
                $http.delete('admin/user/' + a.id).success(function () {
                    a.state = -1;
                }).error(function (d) {
                    console.log(d);
                    alert("删除失败！");
                });
            }
        };

        var sortTypes = ['', 'desc', 'asc'];
        var scoreCounts = 0;

        $scope.sortBy = function(column, e) {
            $scope.params.sortBy = column;
            if (column == "total_score") {
                scoreCounts++;
                $scope.params.sortType = sortTypes[scoreCounts % 3];
            }
            $scope.getData();
        };

        // 获取初始数据
        $scope.getData();

        $scope.getPage = function (page) {
            $scope.params.page = page;
            $scope.getData();
        };
    }]).controller('userDetailCtrl', ['$scope', '$http', '$state', '$stateParams', function ($scope, $http, $state, $stateParams) {
        if ($stateParams.id && $stateParams.id > 0) {
            $http.get('admin/user/' + $stateParams.id).success(function (data) {
                $scope.data = data;
            });
        }

        $scope.states = userStates;

        $scope.fileChanged = function (file) {
            if (!$scope.data.id || !file) {
                return;
            }
            var form = new FormData();
            form.append('file', file);
            $http({
                url: 'admin/user/' + $scope.data.id + '/avatar',
                method: 'POST',
                data: form,
                headers: {
                    'Content-Type': undefined
                }
            }).success(function (data) {
                $scope.data.avatar = data.avatar + '?t=' + new Date().getTime();
            }).error(function () {
                $scope.data.file = null;
                alert('上传头像失败');
            });
        };

        $scope.save = function () {
            if ($scope.data && $scope.data.id) {
                $http.put('admin/user/' + $scope.data.id, $scope.data)
                    .success(function (data) {
                        alert('修改完成！');
                    }).error(function () {
                        alert('修改失败！');
                    });
            }
        }
    }]);
});