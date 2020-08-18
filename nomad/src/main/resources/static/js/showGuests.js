var showGuestsVue = new Vue
({
    el: "#showGuests",
    data:
    {
        guests: {},
    },
    mounted(){
    	 var jwt = localStorage.jwt;
         axios.get("rest/host_all_guests", {headers:{"Authorization": "Bearer " + jwt}})
             .then(response =>
             {
                 this.users = response;
             })
             .catch(response =>
             {
                 this.users = "Empty";
             });
    },
    	
    methods:
    {
        searchGuest: function()
        {
            
        }
    }
});