'use strict';

angular.module('finderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('articleAuthorDetail', {
                parent: 'entity',
                url: '/articleAuthorDetails/:uid',
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
