<template>
    <div>
        <sidebar title="Host panel" :user="user">
            <li>
                <router-link to="/host/">Apartments</router-link>
                <router-link to="/host/guests">Guests</router-link>
                <router-link to="/host/personal_data">Personal data</router-link>
                <router-link to="/host/reservations">Reservations</router-link>
                <router-link to="/host/comments">Comments</router-link>
                <router-link to="/host/edit_apartment">Edit apartment</router-link>
            </li>
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
                    // TODO(Jovan): Handle verification
                    return;
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
            },
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
        padding-top: 10vh;
        background-color: #fafafa;
    }
</style>