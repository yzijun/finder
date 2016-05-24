'use strict';

angular.module('finderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('articleAuthorDetail', {
                parent: 'entity',
                // :uid?:tab 传参数的定义
                url: '/articleAuthorDetails/:uid?:tab',
                data: {
//                    authorities: ['ROLE_USER'],
                	//不需要登录不设定角色就可以显示文章作者详细页面
                	authorities: [],
                    pageTitle: 'finderApp.articleCategory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/articleAuthorDetail/articleAuthorDetail.html',
                        controller: 'ArticleAuthorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
//                        $translatePartialLoader.addPart('articleCategory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
    });
