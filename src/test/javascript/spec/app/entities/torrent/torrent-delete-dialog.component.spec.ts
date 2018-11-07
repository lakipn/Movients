/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MovientsTestModule } from '../../../test.module';
import { TorrentDeleteDialogComponent } from 'app/entities/torrent/torrent-delete-dialog.component';
import { TorrentService } from 'app/entities/torrent/torrent.service';

describe('Component Tests', () => {
    describe('Torrent Management Delete Component', () => {
        let comp: TorrentDeleteDialogComponent;
        let fixture: ComponentFixture<TorrentDeleteDialogComponent>;
        let service: TorrentService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MovientsTestModule],
                declarations: [TorrentDeleteDialogComponent]
            })
                .overrideTemplate(TorrentDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TorrentDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TorrentService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
