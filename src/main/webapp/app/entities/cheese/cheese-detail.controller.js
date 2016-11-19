(function() {
    'use strict';

    angular
        .module('cheesetrackerApp')
        .controller('CheeseDetailController', CheeseDetailController);

    CheeseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cheese', 'Vote'];

    function CheeseDetailController($scope, $rootScope, $stateParams, previousState, entity, Cheese, Vote) {
        var vm = this;

        vm.cheese = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('cheesetrackerApp:cheeseUpdate', function(event, result) {
            vm.cheese = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
