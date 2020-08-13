app = new Vue
({
    el: "#app",
    data:
    {
        users: [],
    },
    mounted()
    {
        var jwt = localStorage.jwt;
        axios.get("rest/admin_all_users", {headers:{"Authorization": "Bearer " + jwt}})
            .then(response =>
            {
                this.users = response;
            })
            .catch(response =>
            {
                this.users = "Empty";
            });
    },

});
