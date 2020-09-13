<template>
    <div class="container">
        <div id="all-apartments">
            <h1>Apartments</h1>
            <table>
                <tr>
                    <th>ID</th>
                    <th>Address</th>
                    <th>Type</th>
                    <th>Price</th>
                    <th>Status</th>
                </tr>
                <!-- TODO(Jovan): Dropdown for each apartment or view separate page -->
                <tr v-for="apartment in apartments">
                    <td>{{apartment.id}}</td>
                    <td>{{apartment.location.address.street}} {{apartment.location.address.streetNo}} {{apartment.location.address.area}}</td>
                    <td>{{apartment.type}}</td>
                    <td>{{apartment.price}}</td>
                    <td>
                        <span v-if="apartment.status === 'ACTIVE'" class="apartment-active">{{apartment.status}}</span>
                        <button v-else type="button" @click="enableApartment(apartment.id)">ENABLE</button>
                    </td>
                </tr>
            </table>
        </div>
        <button v-if="!showForm" @click="showForm = !showForm">+</button>
        <div v-if="showForm" id="apartment-form">
            <div id="apartment-info">
                <h2>Apartment info</h2>
                <div id="apartment-info-container">
                    <div id="general-info">
                        <!-- TODO(Jovan): When room selected disable room input? -->
                        <small class="error">{{errors.apartmentType}}</small>
                        <select name="apartmentType" v-model="apartmentType" required>
                            <option value="" disabled>Type</option>
                            <option value="WHOLE">Whole</option>
                            <option value="ROOM">Room</option>
                        </select>
                        <small class="error">{{errors.noRooms}}</small>
                        <floating-label name="noRooms" placeholder="Number of rooms" type="number" :inputdata.sync="noRooms"></floating-label>
                        <small class="error">{{errors.noGuests}}</small>
                        <floating-label name="noGuests" placeholder="Number of guests" type="number" :inputdata.sync="noGuests"></floating-label>
                        <small class="error">{{errors.price}}</small>
                        <floating-label name="price" placeholder="Price" type="number" :inputdata.sync="price"></floating-label>
                    </div>
                    <div id="amenities">
                        <select id="leftSelect" name="allAmenities" v-model="leftSelected"  multiple>
                            <option v-for="amenity in allAmenities" :value="amenity">{{amenity.name}}</option>
                        </select>
                        <div id="amenity-buttons">
                            <button type="button" @click="addAmenity">&rsaquo;</button>
                            <button type="button" @click="removeAmenity">&lsaquo;</button>
                            <button type="button" @click="clearAmenities">Clear all</button>
                            <button type="button" @click="addAllAmenities">Add all</button>
                        </div>
                        <select id="rightSelect" name="selectedAmenities"  v-model="rightSelected" multiple>
                            <option v-for="amenity in selectedAmenities" :value="amenity">{{amenity.name}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div id="location-info">
                <h3>Location info</h3>
                <small class="error">{{errors.location}}</small>
                <div id="location-info-container">
                    <map-small class="map" v-on:search="parseResult"></map-small>
                    <div id="location-forms">
                        <floating-label name="street" placeholder="Street" type="text" :inputdata.sync="street" readonly></floating-label>
                        <floating-label name="streetNo" placeholder="Street number" type="text" :inputdata.sync="streetNo" readonly></floating-label>
                        <floating-label name="area" placeholder="Area" type="text" :inputdata.sync="area" readonly></floating-label>
                        <floating-label name="areaCode" placeholder="Area code" type="text" :inputdata.sync="areaCode" readonly></floating-label>
                    </div>
                </div>
            </div>
            <div id="available-dates" ref="dates">
                <h2>Available dates</h2>
                <div id="available-dates-container">
                    <div id="dates-selection">
                        <v-date-picker is-inline mode="range" v-model="selectedDate"></v-date-picker>
                        <button type="button" @click="addDate">Add date</button>
                    </div>
                    <div id="selected-dates">
                        <h3>Selected dates:</h3>
                        <ul>
                            <li v-for="(date, index) in availableDates">{{date.start.toLocaleString("en-GB", {dateStyle: "short"})}} - {{date.end.toLocaleString("en-GB", {dateStyle: "short"})}} <button type="button" @click="removeDate(index)">x</button></li>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- TODO(Jovan): Images -->
            <!-- TODO(Jovan): Hidden, dropdown amenities with dual multiselect listbox -->
            
            <b class="error">{{submitMsg}}</b>
            <button type="button" class="button-primary" @click="addApartment">Add apartment</button>
            <button type="button" @click="showForm = !showForm">Cancel</button>
        </div>
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
                lon:               "",
                lat:               "",
                availableDates:    [],
                price:             "",
                allAmenities:      [],
                selectedAmenities: [],
                showForm:          false,
                selectedDate:      null,
                leftSelected:      [],
                rightSelected:     [],
                errors:
                {
                    apartmentType: "",
                    noRooms:       "",
                    noGuests:      "",
                    location:      "",
                    price:         "",

                },
                submitMsg:         "",
            }
        },

        methods:
        {
            enableApartment: function(id)
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    // TODO(Jovan): Handle?
                    return;
                }
                axios.post("rest/host_enable_apartment", id, {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => 
                    {
                        // TODO(Jovan): Refresh?
                        this.$router.go();
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle;
                    });
            },

            validateApartmentType: function()
            {
                if(!this.apartmentType)
                {
                    this.errors.apartmentType = "Select a type";
                    return false;
                }
                return true;
            },

            validateNoRooms: function()
            {
                if(!this.noRooms  || this.noRooms <= 0)
                {
                    this.errors.noRooms = "Must be a positive integer";
                    return false;
                }
                return true;
            },

            validateNoGuests: function()
            {
                if(!this.noGuests || this.noGuests <= 0)
                {
                    this.errors.noGuests = "Must be a positive integer";
                    return false;
                }
                return true;
            },

            validateLocation: function()
            {
                if(!this.area)
                {
                    this.errors.location = "Please choose a location";
                    return false;
                }
                return true;
            },

            validatePrice: function()
            {
                if(!this.price)
                {
                    this.errors.price = "Please add a price";
                    return false;
                }

                if(this.price <= 0)
                {
                    this.errors.price = "Must be a positive integer";
                    return false;
                }

                return true;
            },

            validateInputs: function()
            {
                this.errors =
                {
                    apartmentType: "",
                    noRooms:       "",
                    noGuests:      "",
                    location:      "",
                    price:         "",

                };

                let apartmentTypeValid = this.validateApartmentType();
                let noGuestsValid = this.validateNoGuests();
                let noRoomsValid = this.validateNoRooms();
                let locationValid = this.validateLocation();
                let priceValid = this.validatePrice();

                return apartmentTypeValid && noGuestsValid && noRoomsValid
                    && locationValid && priceValid;
            },

            // NOTE(Jovan): Adds selected available date range to list
            addDate: function()
            {
                if(!this.selectedDate)
                {
                    // TODO(Jovan): Display error
                }
                this.availableDates.push(this.selectedDate);

                // NOTE(Jovan): Clear selection
                this.selectedDate = null;
            },

            // NOTE(Jovan): Removes date from available dates list
            removeDate: function(index)
            {
                if(index <= 0)
                {
                    console.log("Failed to remove " + index);
                    return;
                }
                this.availableDates.splice(index, 1);
            },

            addAmenity: function()
            {
                let selected = this.leftSelected;
                if(!selected)
                {
                    return;
                }
                console.log("adding " + selected);
                this.selectedAmenities = this.selectedAmenities.concat(selected);

                selected.forEach(el => 
                {
                    let index = this.allAmenities.indexOf(el);
                    this.allAmenities.splice(index, 1); 
                });
                this.leftSelected = [];
            },

            removeAmenity: function()
            {
                let selected = this.rightSelected;
                if(!selected)
                {
                    return;
                }
                this.allAmenities = this.allAmenities.concat(selected);

                selected.forEach(el =>
                {
                    let index = this.selectedAmenities.indexOf(el);
                    this.selectedAmenities.splice(index, 1);
                });

                this.rightSelected = [];
            },

            clearAmenities: function()
            {
                this.allAmenities = this.selectedAmenities.concat(this.allAmenities);
                this.selectedAmenities = [];
            },

            addAllAmenities: function()
            {
                this.selectedAmenities = this.allAmenities.concat(this.selectedAmenities);
                this.allAmenities = [];
            },

            // NOTE(Jovan): Parses map search result
            parseResult: function(value)
            {
                this.street = value.address.name;
                this.streetNo = value.address.house_number;
                this.area = value.address.city;
                this.areaCode = value.address.postcode;
                this.lat = value.lat;
                this.lon = value.lon;
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
                console.log("Adding apartment call");
                if(!this.validateInputs())
                {
                    console.log("Invalid inputs");
                    return;
                }
                let apartment =
                    {
                        id: "",
                        type: this.apartmentType,
                        noRooms: this.noRooms,
                        noGuests: this.noGuests,
                        location: 
                            {
                                lat: this.lat,
                                lon: this.lon,
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
                        availableDates: this.availableDates,
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
                    // TODO(Jovan): Ask for login?
                    console.log("No jwt");
                    return;
                }
                axios.post("rest/host_add_apartment", apartment, {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => 
                    {
                        // TODO(Jovan): Handle
                        this.submitMsg = "Apartment added";
                        this.$router.go();
                    })
                    .catch(response =>
                    {
                        // TODO(jovan): Handle
                        this.submitMsg = "Failed adding apartment";
                    })
            }
        },

        mounted()
        {
            this.getApartments();
            this.getAmenities();
        },
    }
</script>

<style scoped>
    #apartment-info-container
    {
        display: flex;
        flex-direction: row;
    }

    #general-info
    {
        display: flex;
        flex-direction: column;
    }
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

    #available-dates-container
    {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    #selected-dates
    {
        display: flex;
        flex-direction: column;
    }

    #location-info-container
    {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    .map
    {
        max-width: 400px;
        max-height: 400px;
    }

    .error
    {
        font-weight: 500;
        color: #f00;
        padding-bottom: 8px;
    }
</style>