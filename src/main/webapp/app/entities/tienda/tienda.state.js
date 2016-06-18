(function() {
    'use strict';

    angular
        .module('ecofruitApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tienda', {
            parent: 'entity',
            url: '/tienda?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecofruitApp.tienda.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tienda/tiendas.html',
                    controller: 'TiendaController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tienda');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tienda.admin', {
            parent: 'entity',
            url: '/tiendaAdmin?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecofruitApp.tienda.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tienda/tiendasAdmin.html',
                    controller: 'TiendaController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tienda');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tienda-detail', {
            parent: 'entity',
            url: '/tienda/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ecofruitApp.tienda.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tienda/tienda-detail.html',
                    controller: 'TiendaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tienda');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tienda', function($stateParams, Tienda) {
                    return Tienda.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('tienda.new', {
            parent: 'tienda',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tienda/tienda-dialog.html',
                    controller: 'TiendaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                direccion: null,
                                ciudad: null,
                                latitud: null,
                                longitud: null,
                                horario: null,
                                telefono: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tienda', null, { reload: true });
                }, function() {
                    $state.go('tienda');
                });
            }]
        })
        .state('tienda.edit', {
            parent: 'tienda',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tienda/tienda-dialog.html',
                    controller: 'TiendaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tienda', function(Tienda) {
                            return Tienda.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tienda', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tienda.delete', {
            parent: 'tienda',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tienda/tienda-delete-dialog.html',
                    controller: 'TiendaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tienda', function(Tienda) {
                            return Tienda.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tienda', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
