'use strict';

angular.module('finderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tag', {
                parent: 'entity',
                url: '/tags',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                    pageTitle: 'finderApp.tag.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tag/tags.html',
                        controller: 'TagController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tag');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('tag.detail', {
                parent: 'entity',
                url: '/tag/{id}',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                    pageTitle: 'finderApp.tag.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tag/tag-detail.html',
                        controller: 'TagDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('tag');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Tag', function($stateParams, Tag) {
                        return Tag.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tag.new', {
                parent: 'tag',
                url: '/new',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tag/tag-dialog.html',
                        controller: 'TagDialogController',
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
                        $state.go('tag', null, { reload: true });
                    }, function() {
                        $state.go('tag');
                    })
                }]
            })
            .state('tag.edit', {
                parent: 'tag',
                url: '/{id}/edit',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tag/tag-dialog.html',
                        controller: 'TagDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Tag', function(Tag) {
                                return Tag.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tag', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tag.delete', {
                parent: 'tag',
                url: '/{id}/delete',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tag/tag-delete-dialog.html',
                        controller: 'TagDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Tag', function(Tag) {
                                return Tag.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tag', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
