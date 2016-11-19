(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .controller('CheeseDialogController', CheeseDialogController);

    CheeseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cheese', 'Vote'];

    function CheeseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cheese, Vote) {
        var vm = this;

        vm.cheese = entity;
        vm.clear = clear;
        vm.save = save;
        vm.votes = Vote.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cheese.id !== null) {
                Cheese.update(vm.cheese, onSaveSuccess, onSaveError);
            } else {
                Cheese.save(vm.cheese, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cheesetrackerApp:cheeseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
