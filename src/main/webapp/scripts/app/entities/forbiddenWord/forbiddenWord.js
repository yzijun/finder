'use strict';

angular.module('finderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('forbiddenWord', {
                parent: 'entity',
                url: '/forbiddenWords',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                    pageTitle: 'finderApp.forbiddenWord.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/forbiddenWord/forbiddenWords.html',
                        controller: 'ForbiddenWordController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('forbiddenWord');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('forbiddenWord.detail', {
                parent: 'entity',
                url: '/forbiddenWord/{id}',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                    pageTitle: 'finderApp.forbiddenWord.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/forbiddenWord/forbiddenWord-detail.html',
                        controller: 'ForbiddenWordDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('forbiddenWord');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ForbiddenWord', function($stateParams, ForbiddenWord) {
                        return ForbiddenWord.get({id : $stateParams.id});
                    }]
                }
            })
            .state('forbiddenWord.new', {
                parent: 'forbiddenWord',
                url: '/new',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/forbiddenWord/forbiddenWord-dialog.html',
                        controller: 'ForbiddenWordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    word: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('forbiddenWord', null, { reload: true });
                    }, function() {
                        $state.go('forbiddenWord');
                    })
                }]
            })
            .state('forbiddenWord.edit', {
                parent: 'forbiddenWord',
                url: '/{id}/edit',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/forbiddenWord/forbiddenWord-dialog.html',
                        controller: 'ForbiddenWordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ForbiddenWord', function(ForbiddenWord) {
                                return ForbiddenWord.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('forbiddenWord', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('forbiddenWord.delete', {
                parent: 'forbiddenWord',
                url: '/{id}/delete',
                data: {
//                    authorities: ['ROLE_USER'],
                	// 文章管理者角色可以访问
                    authorities: ['ROLE_ARTICLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/forbiddenWord/forbiddenWord-delete-dialog.html',
                        controller: 'ForbiddenWordDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ForbiddenWord', function(ForbiddenWord) {
                                return ForbiddenWord.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('forbiddenWord', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
