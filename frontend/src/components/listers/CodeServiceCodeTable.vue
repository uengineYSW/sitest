<template>
    <div>
        <v-data-table
                :headers="headers"
                :items="values"
                :items-per-page="5"
                class="elevation-1"
        ></v-data-table>

        <v-col style="margin-bottom:40px;">
            <div class="text-center">
                <v-dialog
                        v-model="openDialog"
                        width="332.5"
                        fullscreen
                        hide-overlay
                        transition="dialog-bottom-transition"
                >
                    <template v-slot:activator="{ on, attrs }">
                        <v-fab-transition>
                            <v-btn
                                    color="primary"
                                    fab
                                    dark
                                    large
                                    style="position:absolute; bottom: 5%; right: 2%; z-index:99"
                                    @click="openDialog=true;"
                            >
                                <v-icon v-bind="attrs" v-on="on">mdi-plus</v-icon>
                            </v-btn>
                        </v-fab-transition>
                    </template>

                    <CodeServiceCode :offline="offline" class="video-card" :isNew="true" :editMode="true" v-model="newValue" @add="append" v-if="tick"/>
                
                    <v-btn
                            style="postition:absolute; top:2%; right:2%"
                            @click="closeDialog()"
                            depressed 
                            icon 
                            absolute
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-dialog>
            </div>
        </v-col>
    </div>
</template>

<script>
    const axios = require('axios').default;
    import CodeServiceCode from './../CodeServiceCode.vue';

    export default {
        name: 'CodeServiceCodeManager',
        components: {
            CodeServiceCode,
        },
        props: {
            offline: Boolean,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            values: [],
            headers: 
                [
                    { text: "id", value: "id" },
                    { text: "itemCode", value: "itemCode" },
                    { text: "codeNo", value: "codeNo" },
                    { text: "code", value: "code" },
                    { text: "codeName", value: "codeName" },
                    { text: "isSys", value: "isSys" },
                    { text: "isUse", value: "isUse" },
                    { text: "etc", value: "etc" },
                    { text: "etc1", value: "etc1" },
                    { text: "etc2", value: "etc2" },
                    { text: "etc3", value: "etc3" },
                    { text: "etc4", value: "etc4" },
                    { text: "etc5", value: "etc5" },
                ],
            code : [],
            newValue: {},
            tick : true,
            openDialog : false,
        }),
        async created() {
            if(this.offline){
                if(!this.values) this.values = [];
                return;
            }

            var temp = await axios.get(axios.fixUrl('/codes'))
            temp.data._embedded.codes.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])
            this.values = temp.data._embedded.codes;

            this.newValue = {
                'itemCode': '',
                'codeNo': '',
                'code': '',
                'codeName': '',
                'isSys': '',
                'isUse': '',
                'etc': '',
                'etc1': '',
                'etc2': '',
                'etc3': '',
                'etc4': '',
                'etc5': '',
            }
        },
        methods: {
            closeDialog(){
                this.openDialog = false
            },
            append(value){
                this.tick = false
                this.newValue = {}
                this.values.push(value)
                
                this.$emit('input', this.values);

                this.$nextTick(function(){
                    this.tick=true
                })
            },
        }
    }
</script>

