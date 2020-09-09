<template>
    <div>
        <!-- TODO(Jovan): Make into a separate component -->
        <sidebar title="Admin panel" :user="user">
            <li><router-link to="/admin">Overview</router-link></li>
            <li><router-link to="/admin/amenities">Amenities</router-link></li>
            <li><router-link to="/admin/all_users">All users</router-link></li>
            <li><router-link to="/admin/reservations">Reservations</router-link></li>
            <li><router-link to="/admin/apartments">Apartments</router-link></li>
        </sidebar>

        <div id="main-content">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                user: { username: "placeholder" },
                jwt: "",
            }
        },
        methods:
        {
            verifyLogin: function()
            {
                this.jwt = localStorage.jwt;
                if(!this.jwt)
                {
                    // TODO(Jovan): Handle user not logged in!
                }
            },
            getUser: function()
            {
                axios.get("rest/get_user", {headers: {"Authorization": "Bearer " + this.jwt}})
                    .then(response =>
                    {
                        this.user = response.data;
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle invalid response
                    });
            }
        },
        mounted()
        {
            this.verifyLogin();
            this.getUser();
        },
    }
</script>

<style scoped>

    #main-content
    {
        margin-left: 15vw;
        background-color: #fafafa;
        height: 100vh;
    }
    
</style>
