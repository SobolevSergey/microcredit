$(function ($) {
    var pstyle = 'background-color: #F5F6F7; border: 1px solid #dfdfdf; padding: 5px;';
    $('#container').w2layout({
        name: 'layout',
        panels: [
            { type: 'top', size: 50, resizable: true, style: pstyle, content: 'top' },
            { type: 'left', size: 200, resizable: true, style: pstyle, content: 'left' },
            { type: 'main', overflow: 'hidden',
                style: 'background-color: white; border: 1px solid silver; border-top: 0px; padding: 10px;',
                form: {
                    name: 'form',
                    url: 'server/post',
                    header: 'Field Types',
                    formURL: 'data/form.html',
                    fields: [
                        { name: 'field_text', type: 'text', required: true },
                        { name: 'field_alpha', type: 'alphaNumeric', required: true },
                        { name: 'field_int', type: 'int', required: true },
                        { name: 'field_float', type: 'float', required: true },
                        { name: 'field_date', type: 'date' },
                        { name: 'field_list', type: 'select', required: true,
                            options: { items: ['Adams, John', 'Johnson, Peter', 'Lewis, Frank', 'Cruz, Steve', 'Donnun, Nick'] } },
                        { name: 'field_enum', type: 'enum', required: true,
                            options: { items: ['Adams, John', 'Johnson, Peter', 'Lewis, Frank', 'Cruz, Steve', 'Donnun, Nick'] } },
                        { name: 'field_textarea', type: 'text'}
                    ],
                    actions: {
                        reset: function () {
                            this.clear();
                        },
                        save: function () {
                            var obj = this;
                            this.save({}, function (data) {
                                if (data.status == 'error') {
                                    console.log('ERROR: ' + data.message);
                                    return;
                                }
                                obj.clear();
                            });
                        }
                    }
                }
            },
            { type: 'right', size: 200, resizable: true, style: pstyle, content: 'right' },
            { type: 'bottom', size: 50, resizable: true, style: pstyle, content: 'bottom' }
        ]
    });
});
/*
 tabs:{
 name: 'tabs',
 active: 'tab1',
 tabs: [
 { id: 'tab1', caption: 'Основная информация'  },
 { id: 'tab3', caption: 'Tab 3' },
 { id: 'tab2', caption: 'Tab 2'},
 { id: 'tab4', caption: 'Tab 4'},
 { id: 'tab5', caption: 'Tab 5' }
 ],onClick: function (event) {
 //   w2ui.layout.html('main', 'Active tab: '+ event.target);
 }
 }
 */