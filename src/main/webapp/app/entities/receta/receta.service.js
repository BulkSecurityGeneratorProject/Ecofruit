(function() {
    'use strict';
    angular
        .module('ecofruitApp')
        .factory('Receta', Receta);

    Receta.$inject = ['$resource', 'DateUtils'];

    function Receta ($resource, DateUtils) {
        var resourceUrl =  'api/recetas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    }
                    return data;
                }
            },
            'getMisrecetas':{
                method :'GET',
                isArray:true,
                url:'api/Misrecetas2'
            },
           /* 'getRecetasComentarios':{
                method :'GET',
                isArray:true,
                url:'api/recetasComentarios/{id}'
            },*/
            'update': { method:'PUT' }
        });
    }
})();
