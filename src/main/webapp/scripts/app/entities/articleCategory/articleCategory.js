'use strict';

angular.module('finderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('articleCategory', {
                parent: 'entity',
                url: '/articleCategorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'finderApp.articleCategory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/articleCategory/articleCategorys.html',
                        controller: 'ArticleCategoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('articleCategory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('articleCategory.detail', {
                parent: 'entity',
                url: '/articleCategory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'finderApp.articleCategory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/articleCategory/articleCategory-detail.html',
                        controller: 'ArticleCategoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('articleCategory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ArticleCategory', function($stateParams, ArticleCategory) {
                        return ArticleCategory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('articleCategory.new', {
                parent: 'articleCategory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/articleCategory/articleCategory-dialog.html',
                        controller: 'ArticleCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('articleCategory', null, { reload: true });
                    }, function() {
                        $state.go('articleCategory');
                    })
                }]
            })
            .state('articleCategory.edit', {
                parent: 'articleCategory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/articleCategory/articleCategory-dialog.html',
                        controller: 'ArticleCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ArticleCategory', function(ArticleCategory) {
                                return ArticleCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('articleCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('articleCategory.delete', {
                parent: 'articleCategory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/articleCategory/articleCategory-delete-dialog.html',
                        controller: 'ArticleCategoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ArticleCategory', function(ArticleCategory) {
                                return ArticleCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('articleCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
