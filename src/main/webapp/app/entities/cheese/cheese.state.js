(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cheese', {
            parent: 'entity',
            url: '/cheese',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cheesetrackerApp.cheese.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cheese/cheeses.html',
                    controller: 'CheeseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cheese');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cheese-detail', {
            parent: 'entity',
            url: '/cheese/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'cheesetrackerApp.cheese.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cheese/cheese-detail.html',
                    controller: 'CheeseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cheese');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cheese', function($stateParams, Cheese) {
                    return Cheese.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cheese',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cheese-detail.edit', {
            parent: 'cheese-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheese/cheese-dialog.html',
                    controller: 'CheeseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cheese', function(Cheese) {
                            return Cheese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cheese.new', {
            parent: 'cheese',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheese/cheese-dialog.html',
                    controller: 'CheeseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                milk: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cheese', null, { reload: 'cheese' });
                }, function() {
                    $state.go('cheese');
                });
            }]
        })
        .state('cheese.edit', {
            parent: 'cheese',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheese/cheese-dialog.html',
                    controller: 'CheeseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cheese', function(Cheese) {
                            return Cheese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cheese', null, { reload: 'cheese' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cheese.delete', {
            parent: 'cheese',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cheese/cheese-delete-dialog.html',
                    controller: 'CheeseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cheese', function(Cheese) {
                            return Cheese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cheese', null, { reload: 'cheese' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
