<template>
    <div>
        <sidebar :user="user">
            <li><router-link to="/guest">Overview</router-link></li>
            <li><router-link to="/guest/reservations">Reservations</router-link></li>
            <li><router-link to="/guest/apartments">Apartments</router-link></li>
            <li><router-link to="/guest/personal_data">Personal data</router-link></li>
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
                user: {},
            }
        },

        methods:
        {
            getUser: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    // TODO(Jovan): handle?
                    return;
                }

                axios.get("rest/get_user", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => 
                    {
                        this.user = response.data;
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle?
                    });
            }
        },
        mounted()
        {
            this.getUser();
        },
    };
</script>

<style scoped>
    #main-content
    {
        margin-left: 15vw;
        background-color: #fafafa;
    }
</style>