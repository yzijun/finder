'use strict';

angular.module('finderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('articleReply', {
                parent: 'entity',
                url: '/articleReplys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'finderApp.articleReply.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/articleReply/articleReplys.html',
                        controller: 'ArticleReplyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('articleReply');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('articleReply.detail', {
                parent: 'entity',
                url: '/articleReply/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'finderApp.articleReply.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/articleReply/articleReply-detail.html',
                        controller: 'ArticleReplyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('articleReply');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ArticleReply', function($stateParams, ArticleReply) {
                        return ArticleReply.get({id : $stateParams.id});
                    }]
                }
            })
            .state('articleReply.new', {
                parent: 'articleReply',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/articleReply/articleReply-dialog.html',
                        controller: 'ArticleReplyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    content: null,
                                    published: null,
                                    createdDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('articleReply', null, { reload: true });
                    }, function() {
                        $state.go('articleReply');
                    })
                }]
            })
            .state('articleReply.edit', {
                parent: 'articleReply',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/articleReply/articleReply-dialog.html',
                        controller: 'ArticleReplyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ArticleReply', function(ArticleReply) {
                                return ArticleReply.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('articleReply', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('articleReply.delete', {
                parent: 'articleReply',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/articleReply/articleReply-delete-dialog.html',
                        controller: 'ArticleReplyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ArticleReply', function(ArticleReply) {
                                return ArticleReply.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('articleReply', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
