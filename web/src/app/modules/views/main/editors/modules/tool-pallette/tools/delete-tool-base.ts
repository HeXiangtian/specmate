import { TypeAwareToolBase } from './type-aware-tool-base';
import { IContainer } from '../../../../../../../model/IContainer';
import { SpecmateDataService } from '../../../../../../data/modules/data-service/services/specmate-data.service';
import { SelectedElementService } from '../../../../../side/modules/selected-element/services/selected-element.service';
import { Id } from '../../../../../../../util/id';
import { IModelNode } from '../../../../../../../model/IModelNode';
import { IModelConnection } from '../../../../../../../model/IModelConnection';
import { Proxy } from '../../../../../../../model/support/proxy';
import { Arrays } from '../../../../../../../util/arrays';
import { Url } from '../../../../../../../util/url';
import { GraphTransformer } from '../util/graphTransformer';

export abstract class DeleteToolBase extends TypeAwareToolBase {

    public icon = 'eraser';
    public color = 'danger';
    public cursor = 'alias';
    public name = 'tools.delete';
    public done = false;
    public sticky = false;

    public selectedElements: IContainer[];
    private graphTransformer: GraphTransformer;

    constructor(private parent: IContainer,
        private dataService: SpecmateDataService,
        selectedElementService: SelectedElementService) {
        super(selectedElementService);
        this.selectedElements = [];
        this.graphTransformer = new GraphTransformer(dataService, selectedElementService, parent);
    }

    activate(): void {
        this.done = false;
    }

    deactivate(): void { }

    click(event: MouseEvent, zoom: number): Promise<void> {
        return Promise.resolve();
    }

    select(element: IContainer): Promise<void> {
        let compoundId: string = Id.uuid;
        return this.graphTransformer.deleteElementAndDeselect(element, compoundId).then(() => {
            this.done = true;
        });
    }
}
