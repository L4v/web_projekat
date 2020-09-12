<template>
    <div>
        <h1>Apartments</h1>
        <table>
            <tr>
                <th>ID</th>
                <th>Address</th>
            </tr>
            <!-- TODO(Jovan): Dropdown for each apartment or view separate page -->
            <tr v-for="apartment in apartments">
                <td>{{apartment.id}}</td>
                <td>{{apartment.location.address.street}} {{apartment.location.address.streetNo}} {{apartment.location.address.area}}</td>
            </tr>
        </table>

        <button v-if="!showForm" @click="showForm = !showForm">+</button>
        <form v-if="showForm" @submit.prevent="addApartment" id="apartment-form">
            <select name="apartmentType" v-model="apartmentType" required>
                <option value="" disabled>Type</option>
                <option value="whole">Whole</option>
                <option value="room">Room</option>
            </select>
            <floating-label name="noRooms" placeholder="Number of rooms" type="number" :inputdata.sync="noRooms"></floating-label>
            <floating-label name="noGuests" placeholder="Number of guests" type="number" :inputdata.sync="noGuests"></floating-label>
            <!--
            <floating-label name="street" placeholder="Street" type="text" :inputdata.sync="street"></floating-label>
            <floating-label name="streetNo" placeholder="Street number" type="text" :inputdata.sync="streetNo"></floating-label>
            <floating-label name="area" placeholder="Area" type="text" :inputdata.sync="area"></floating-label>
            <floating-label name="areaCode" placeholder="Area code" type="text" :inputdata.sync="areaCode"></floating-label>
            -->
            <!--<map-small></map-small>-->
            <h2>Result: {{searchRes[0].display_name}}</h2>
            <!-- TODO(Jovan): Lon & lat -->
            <!-- TODO(Jovan): adding multiple dates -->
            <v-date-picker mode="range" v-model="availableDates" :input-props="{placeholder: 'Available dates'}"></v-date-picker>
            <!-- TODO(Jovan): Images -->
            <floating-label name="price" placeholder="Price" type="number" :inputdata.sync="price"></floating-label>
            <!-- TODO(Jovan): Hidden, dropdown amenities with dual multiselect listbox -->
            <div id="amenities">
                <select name="allAmenities"  multiple>
                    <option v-for="amenity in allAmenities">{{amenity.name}}</option>
                </select>
                <div id="amenity-buttons">
                    <button>&laquo;</button>
                    <button>&lsaquo;</button>
                    <button>Clear all</button>
                    <button>Add all</button>
                </div>
                <select name="selectedAmenities" multiple>
                    <option v-for="amenity in selectedAmenities">{{amenity.name}}</option>
                </select>
            </div>
            <button @click="addApartment">Add apartment</button>
        </form>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                apartments: [],
                apartmentType:     "",
                noRooms:           "",
                noGuests:          "",
                street:            "",
                streetNo:          "",
                area:              "",
                areaCode:          "",
                availableDates:    null,
                price:             "",
                allAmenities:      [],
                selectedAmenities: [],
                showForm: false,
                searchRes: "",
            }
        },

        methods:
        {
            searchLocation: function()
            {
                axios.get("https://eu1.locationiq.com/v1/search.php", 
                    {
                        params: 
                        {
                            key: locationiq.key,
                            q: "Balzakova 69",
                            format: "json",
                        }
                    })
                    .then(response => 
                    {
                        this.searchRes = response.data;
                    });
            },
            getApartments: function()
            {
                axios.get("rest/host_all_apartments", {headers: {"Authorization": "Bearer " + localStorage.jwt}})
                    .then(response => {
                        this.apartments = response.data;
                    })
                    .catch(response => {
                        // TODO(Jovan): Handle bad response
                    });
            },

            getAmenities: function()
            {
                axios.get("rest/get_amenities")
                    .then(response =>
                    {
                        this.allAmenities = response.data;
                    });
            },

            // TODO(Jovan): Validation
            addApartment: function()
            {
                let apartment =
                    {
                        id: "",
                        type: this.apartmentType,
                        noRooms: this.noRooms,
                        noGuests: this.noGuests,
                        location: 
                            {
                                lat: "0",
                                lon: "0",
                                address: 
                                    {
                                        street: this.street,
                                        streetNumber: this.streetNo,
                                        area: this.area,
                                        areaCode: this.areaCode
                                    },
                            },
                        rentDates: [],
                        // TODO(Jovan): add dates
                        availableDates: [],
                        host: "",
                        comments: [],
                        images: [],
                        price: this.price,
                        checkinTime: {},
                        checkoutTime: {},
                        status: "INACTIVE",
                        amenities: this.selectedAmenities,
                        reservations: [],
                    };
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    return;
                }
                axios.post("rest/host_add_apartment", apartment, {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => 
                    {
                        // TODO(Jovan): Handle
                    })
                    .catch(response =>
                    {
                        // TODO(jovan): Handle
                    })
            }
        },

        mounted()
        {
            this.getApartments();
            this.getAmenities();
            this.searchLocation();
        },
    }
</script>

<style scoped>
    #amenities
    {
        display: flex;
        flex-direction: row;
        flex: 2 1 2;
    }

    #amenities select
    {
        height: 100%;
        min-height: 200px;
        min-width: 200px;
    }

    #amenity-buttons
    {
        display: flex;
        flex-direction: column;
    }
</style>