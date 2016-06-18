(function() {
    'use strict';

    angular
        .module('ecofruitApp')
        .controller('TiendaDeleteController',TiendaDeleteController);

    TiendaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tienda'];

    function TiendaDeleteController($uibModalInstance, entity, Tienda) {
        var vm = this;

        vm.tienda = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tienda.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
