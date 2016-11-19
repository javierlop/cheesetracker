(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .controller('CheeseController', CheeseController);

    CheeseController.$inject = ['$scope', '$state', 'Cheese'];

    function CheeseController ($scope, $state, Cheese) {
        var vm = this;
        
        vm.cheeses = [];

        loadAll();

        function loadAll() {
            Cheese.query(function(result) {
                vm.cheeses = result;
            });
        }
    }
})();
