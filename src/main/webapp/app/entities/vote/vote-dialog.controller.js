(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .controller('VoteDialogController', VoteDialogController);

    VoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vote', 'User', 'Cheese'];

    function VoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vote, User, Cheese) {
        var vm = this;

        vm.vote = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.cheeses = Cheese.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vote.id !== null) {
                Vote.update(vm.vote, onSaveSuccess, onSaveError);
            } else {
                Vote.save(vm.vote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('cheesetrackerApp:voteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
