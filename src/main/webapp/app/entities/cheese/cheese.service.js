(function() {
    'use strict';
    angular
        .module('cheesetrackerApp')
        .factory('Cheese', Cheese);

    Cheese.$inject = ['$resource'];

    function Cheese ($resource) {
        var resourceUrl =  'api/cheeses/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
