(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .controller('VoteDetailController', VoteDetailController);

    VoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vote', 'User', 'Cheese'];

    function VoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Vote, User, Cheese) {
        var vm = this;

        vm.vote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cheesetrackerApp:voteUpdate', function(event, result) {
            vm.vote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
