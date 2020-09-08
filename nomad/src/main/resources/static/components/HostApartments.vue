<template>
    <div>
        <table>
            <tr>
                <th>ID</th>
                <th>Address</th>
            </tr>
            <tr v-for="apartment in apartments">
            <td>{{apartment.id}}</td>
            <td>{{apartment.location.address}}</td>
            </tr>
        </table>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                apartments: [],
            }
        },

        methods:
        {
            getApartments: function()
            {
                axios.get("rest/host_all_apartments", {headers: {"Authorization": "Bearer " + localStorage.jwt}})
                    .then(response => {
                        this.apartments = response.data;
                    })
                    .catch(response => {
                        // TODO(Jovan): Handle bad response
                    });
            }
        },
    }
</script>