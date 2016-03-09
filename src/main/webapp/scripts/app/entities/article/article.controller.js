'use strict';

angular.module('finderApp')
    .controller('ArticleController', function ($scope, $state, $sce, $http, DataUtils, Article, ArticleSearch, ParseLinks) {

        $scope.articles = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        // 自定义每页显示的数量  github Fix #2625
        $scope.pageSize = 20;
        $scope.loadAll = function() {
            Article.query({page: $scope.page - 1, size: $scope.pageSize, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.articles = result;
                //angular对html转义,增加信任$sce.trustAsHtml
                angular.forEach($scope.articles, function (article) {
                	article.content = $sce.trustAsHtml(article.content);
                });
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ArticleSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.articles = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.article = {
                title: null,
                firstImg: null,
                firstImgContentType: null,
                content: null,
                createdDate: null,
                id: null
            };
        };

        $scope.abbreviate = DataUtils.abbreviate;

        $scope.byteSize = DataUtils.byteSize;
        
        // 设置文章禁止发布的状态
        $scope.updatePublished = function () {
        	var ids = [];
        	// 取得checkbox没有选择的更新发布状态
        	$(":checkbox:not(:checked)").each(function(){
        		ids.push($(this).val());
			});
        	if (ids.length > 0) {
        		// 用$http.post发请求
                $http.post('api/updatePublished', JSON.stringify(ids)).success(function (result) {
                	 $scope.articles = result;
                });
        	}
        }
        
    });
