'use strict';

/**
 * Named playlist service.
 */
angular.module('music').factory('NamedPlaylist', function($rootScope, $modal, Restangular, toaster) {
  $rootScope.playlists = []
  $rootScope.userList=[];
  var service = {};

  service = {
    update: function() {
      Restangular.one('playlist').get({
        limit: 1000
      }).then(function(data) {
        $rootScope.playlists = data.items;
      });
    },

    addToPlaylist: function(playlist, tracks) {
      Restangular.one('playlist/' + playlist.id + '/multiple').put({
        ids: _.pluck(tracks, 'id'),
        clear: false
      }).then(function() {
        toaster.pop('success', 'Track' + (tracks.length > 1 ? 's' : '') + ' added to ' + playlist.name,
          _.pluck(tracks, 'title').join('\n'));
      });
    },

    removeTrack: function(playlist, order) {
      return Restangular.one('playlist/' + playlist.id, order).remove();
    },

    remove: function(playlist) {
      return Restangular.one('playlist/' + playlist.id).remove().then(function() {
        $rootScope.playlists = _.reject($rootScope.playlists, function(p) {
          return p.id === playlist.id;
        });
      });
    },

    moveTrack: function(playlist, order, neworder) {
      return Restangular.one('playlist/' + playlist.id, order).post('move', {
        neworder: neworder
      });
    },

    createPlaylist: function(tracks) {
      $modal.open({
        templateUrl: 'partial/modal.createplaylist.html',
        controller: function($scope, $modalInstance) {
          'ngInject';
          $scope.ok = function (name,type,read_type) {
          if (typeof type === 'undefined') {
            type = false; // set the variable to a boolean value
          }
          if (typeof read_type === 'undefined') {
                      read_type = false; // set the variable to a boolean value
          }
          console.log("playlist name is: "+ name+"\ntype of playlist is "+type+" read type "+read_type+"\n\n")
          let message={name: name,type:type,read_type:read_type}
            $modalInstance.close(message);
          };

          $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
          };
        }
      }).result.then(function(message) {
      console.log("In result.then function playlist name is: "+ message.name+"\ntype of playlist is "+message.type+"\n\n")
        Restangular.one('playlist').put({
          name: message.name,
          type: message.type,
          read_type: message.read_type
        }).then(function(data) {
          $rootScope.playlists.push(data.item);
          service.addToPlaylist(data.item, tracks);
          toaster.pop('success', 'Playlist created',message.name);
          toaster.pop('success', 'Playlist Type', message.type);
          toaster.pop('success', 'Playlist Read only Type', message.read_type);
        });
      });
    }
  };

  $rootScope.loadUsersList = function() {
      Restangular.one('user/list').get({ limit: 100 }).then(function(data) {
        $rootScope.userList = data.users;
      });
    };
   $rootScope.loadUsersList();


  return service;
});