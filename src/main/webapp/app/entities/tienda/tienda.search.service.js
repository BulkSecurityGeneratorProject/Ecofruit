(function() {
    'use strict';

    angular
        .module('ecofruitApp')
        .factory('TiendaSearch', TiendaSearch);

    TiendaSearch.$inject = ['$resource'];

    function TiendaSearch($resource) {
        var resourceUrl =  'api/_search/tiendas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
