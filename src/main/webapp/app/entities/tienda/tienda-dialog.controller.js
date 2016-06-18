(function() {
    'use strict';

    angular
        .module('ecofruitApp')
        .controller('TiendaDialogController', TiendaDialogController);

    TiendaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tienda','NgMap', 'User'];

    function TiendaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tienda, NgMap, User) {
        var vm = this;

        vm.tienda = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $scope.lat = [];
        $scope.lng = [];
        $scope.placeChanged = function() {
            $scope.place = this.getPlace();

            $scope.map.setCenter($scope.place.geometry.location);
            var pos = $scope.place.geometry.location.toString();

            $scope.lat = $scope.place.geometry.location.lat();
            $scope.lng = $scope.place.geometry.location.lng();

            $scope.tienda.latitud = $scope.lat;
            $scope.tienda.longitud = $scope.lng;
        }

        $scope.locationChanged = function() {
            $scope.locat = this.getPlace();
        }

        NgMap.getMap().then(function(map) {
            $scope.map = map;
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tienda.id !== null) {
                Tienda.update(vm.tienda, onSaveSuccess, onSaveError);
            } else {
                Tienda.save(vm.tienda, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ecofruitApp:tiendaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
