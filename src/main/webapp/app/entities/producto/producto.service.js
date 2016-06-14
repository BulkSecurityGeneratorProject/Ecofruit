(function() {
    'use strict';
    angular
        .module('ecofruitApp')
        .factory('Producto', Producto);

    Producto.$inject = ['$resource'];

    function Producto ($resource) {
        var resourceUrl =  'api/productos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getProductosFruta':{
                method :'GET',
                isArray:true,
                url:'api/productosFruta'
            },
            'getProductosCarne':{
                method :'GET',
                isArray:true,
                url:'api/productosCarne'
            },
            'getProductosVerdura':{
                method :'GET',
                isArray:true,
                url:'api/productosVerdura'
            },
            'getProductosBatido':{
                method :'GET',
                isArray:true,
                url:'api/productosBatido'
            },
            'update': { method:'PUT' }
        });
    }
})();
