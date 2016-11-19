(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .controller('CheeseDeleteController',CheeseDeleteController);

    CheeseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cheese'];

    function CheeseDeleteController($uibModalInstance, entity, Cheese) {
        var vm = this;

        vm.cheese = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cheese.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
