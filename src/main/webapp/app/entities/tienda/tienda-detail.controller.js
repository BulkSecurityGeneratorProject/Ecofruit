(function() {
    'use strict';

    angular
        .module('ecofruitApp')
        .controller('TiendaDetailController', TiendaDetailController);

    TiendaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Tienda', 'User'];

    function TiendaDetailController($scope, $rootScope, $stateParams, entity, Tienda, User) {
        var vm = this;

        vm.tienda = entity;

        var unsubscribe = $rootScope.$on('ecofruitApp:tiendaUpdate', function(event, result) {
            vm.tienda = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
