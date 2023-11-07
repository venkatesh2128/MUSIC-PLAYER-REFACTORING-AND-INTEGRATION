"use strict";

/**
 * Signup controller.
 */
angular.module("music").controller("Signup", function ($rootScope, $scope, $state, Restangular) {
  $scope.signup = function () {
    Restangular.one("user")
      .put($scope.user)
      .then(function () {
        $state.transitionTo("login");
      });
  };
});
