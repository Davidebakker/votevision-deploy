var simplemaps_countrymap_mapdata={
  main_settings: {
   //General settings
    width: "responsive", //'700' or 'responsive'
    background_color: "black",
    background_transparent: "yes",
    border_color: "#ffffff",
    
    //State defaults
    state_description: "State description",
    state_color: "#88A4BC",
    state_hover_color: "#3B729F",
    state_url: "",
    border_size: 1.5,
    all_states_inactive: "no",
    all_states_zoomable: "no",
    
    //Location defaults
    location_description: "Location description",
    location_url: "",
    location_color: "#FF0067",
    location_opacity: 0.8,
    location_hover_opacity: 1,
    location_size: 25,
    location_type: "square",
    location_image_source: "frog.png",
    location_border_color: "#FFFFFF",
    location_border: 2,
    location_hover_border: 2.5,
    all_locations_inactive: "no",
    all_locations_hidden: "no",
    
    //Label defaults
    label_color: "#ffffff",
    label_hover_color: "#ffffff",
    label_size: "13",
    label_font: "Arial",
    label_display: "auto",
    label_scale: "yes",
    hide_labels: "no",
    hide_eastern_labels: "no",
   
    //Zoom settings
    zoom: "yes",
    manual_zoom: "yes",
    back_image: "no",
    initial_back: "no",
    initial_zoom: "-1",
    initial_zoom_solo: "no",
    region_opacity: 1,
    region_hover_opacity: 0.6,
    zoom_out_incrementally: "yes",
    zoom_percentage: 0.99,
    zoom_time: 0.5,
    
    //Popup settings
    popup_color: "white",
    popup_opacity: 0.9,
    popup_shadow: 1,
    popup_corners: 5,
    popup_font: "12px/1.5 Verdana, Arial, Helvetica, sans-serif",
    popup_nocss: "no",
    
    //Advanced settings
    div: "map",
    auto_load: "yes",
    url_new_tab: "yes",
    images_directory: "default",
    fade_time: 0.1,
    link_text: "View Website",
    popups: "detect",
    state_image_url: "",
    state_image_position: "",
    location_image_url: "",
    border_hover_color: "#0027ff",
    border_hover_size: "2"
  },
  state_specific: {
    NLDR: {
      name: "Drenthe",
      description: "Er zijn 653 stembureaus in de provincie Drenthe, waarvoor gegevens bekend zijn.",
      url: "",
      onclick: function() {
        window.location.href = `/results/Drenthe`;
      } },
    NLFL: {
      name: "Flevoland",
      description: "Er zijn 418 stembureaus in de provincie Flevoland, waarvoor gegevens bekend zijn.",
      url: "",
      onclick: function() {
        window.location.href = `/results/Flevoland`;
      }},
    NLFR: {
      name: "Friesland",
      description: "Er zijn 1.081 stembureaus in de provincie Friesland, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/friesland/#verkiezingsuitslagen"
    },
    NLGE: {
      name: "Gelderland",
      description: "Er zijn 2.677 stembureaus in de provincie Gelderland, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/gelderland/#verkiezingsuitslagen"
    },
    NLGR: {
      name: "Groningen",
      description: "Er zijn 938 stembureaus in de provincie Groningen, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/groningen/#verkiezingsuitslagen"
    },
    NLLI: {
      name: "Limburg",
      description: "Er zijn 1.377 stembureaus in de provincie Limburg, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/limburg/#verkiezingsuitslagen"
    },
    NLNB: {
      name: "Noord-Brabant",
      description: "Er zijn 2.729 stembureaus in de provincie Noord-Brabant, waarvoor gegevens bekend zijn. ",
      url: "https://allecijfers.nl/provincie/noord-brabant/#verkiezingsuitslagen"
    },
    NLNH: {
      name: "Noord-Holland",
      description: "Er zijn 3.277 stembureaus in de provincie Noord-Holland, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/noord-holland/#verkiezingsuitslagen"
    },
    NLOV: {
      name: "Overijssel",
      description: "Er zijn 1.517 stembureaus in de provincie Overijssel, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/overijssel/#verkiezingsuitslagen"
    },
    NLUT: {
      name: "Utrecht",
      description: "Er zijn 1.560 stembureaus in de provincie Utrecht, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/utrecht/#verkiezingsuitslagen"
    },
    NLZE: {
      name: "Zeeland",
      description: "Er zijn 520 stembureaus in de provincie Zeeland, waarvoor gegevens bekend zijn.",
      url: "https://allecijfers.nl/provincie/zeeland/#verkiezingsuitslagen"
    },
    NLZH: {
      name: "Zuid-Holland",
      description: "Er zijn 4.178 stembureaus in de provincie Zuid-Holland, waarvoor gegevens bekend zijn. ",
      url: "https://allecijfers.nl/provincie/zuid-holland/#verkiezingsuitslagen"
    }
  },
  locations: {},
  labels: {
    "0": {
      name: "Drenthe",
      parent_id: "NLDR",
      x: 783.3,
      y: 277.3
    },
    "1": {
      name: "Flevoland",
      parent_id: "NLFL",
      x: 552.3,
      y: 379.3
    },
    "2": {
      name: "Friesland",
      parent_id: "NLFR",
      x: 602.1,
      y: 205.6
    },
    "3": {
      name: "Gelderland",
      parent_id: "NLGE",
      x: 628.8,
      y: 533.9
    },
    "4": {
      name: "Groningen",
      parent_id: "NLGR",
      x: 776.9,
      y: 128.7
    },
    "5": {
      name: "Limburg",
      parent_id: "NLLI",
      x: 623.2,
      y: 795.9
    },
    "6": {
      name: "Noord-Brabant",
      parent_id: "NLNB",
      x: 535.6,
      y: 708.5
    },
    "7": {
      name: "Noord-Holland",
      parent_id: "NLNH",
      x: 434.7,
      y: 318.8
    },
    "8": {
      name: "Overijssel",
      parent_id: "NLOV",
      x: 727.9,
      y: 420.5
    },
    "9": {
      name: "Utrecht",
      parent_id: "NLUT",
      x: 502.5,
      y: 528.1
    },
    "10": {
      name: "Zeeland",
      parent_id: "NLZE",
      x: 227.8,
      y: 786.7
    },
    "11": {
      name: "Zuid-Holland",
      parent_id: "NLZH",
      x: 343.5,
      y: 580.9
    },
    NLDR: {
      name: "Drenthe",
      parent_id: "NLDR",
      x: 783.3,
      y: 277.3
    },
    NLFL: {
      name: "Flevoland",
      parent_id: "NLFL",
      x: 552.3,
      y: 379.3
    },
    NLFR: {
      name: "Friesland",
      parent_id: "NLFR",
      x: 602.1,
      y: 205.6
    },
    NLGE: {
      name: "Gelderland",
      parent_id: "NLGE",
      x: 628.8,
      y: 533.9
    },
    NLGR: {
      name: "Groningen",
      parent_id: "NLGR",
      x: 776.9,
      y: 128.7
    },
    NLLI: {
      name: "Limburg",
      parent_id: "NLLI",
      x: 623.2,
      y: 795.9
    },
    NLNB: {
      name: "Noord-Brabant",
      parent_id: "NLNB",
      x: 535.6,
      y: 708.5
    },
    NLNH: {
      name: "Noord-Holland",
      parent_id: "NLNH",
      x: 434.7,
      y: 318.8
    },
    NLOV: {
      name: "Overijssel",
      parent_id: "NLOV",
      x: 727.9,
      y: 420.5
    },
    NLUT: {
      name: "Utrecht",
      parent_id: "NLUT",
      x: 502.5,
      y: 528.1
    },
    NLZE: {
      name: "Zeeland",
      parent_id: "NLZE",
      x: 227.8,
      y: 786.7
    },
    NLZH: {
      name: "Zuid-Holland",
      parent_id: "NLZH",
      x: 343.5,
      y: 580.9
    }
  },
  legend: {
    entries: []
  },
  regions: {}
};