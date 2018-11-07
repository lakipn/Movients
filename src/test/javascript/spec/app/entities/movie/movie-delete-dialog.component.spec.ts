/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MovientsTestModule } from '../../../test.module';
import { MovieDeleteDialogComponent } from 'app/entities/movie/movie-delete-dialog.component';
import { MovieService } from 'app/entities/movie/movie.service';

describe('Component Tests', () => {
    describe('Movie Management Delete Component', () => {
        let comp: MovieDeleteDialogComponent;
        let fixture: ComponentFixture<MovieDeleteDialogComponent>;
        let service: MovieService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MovientsTestModule],
                declarations: [MovieDeleteDialogComponent]
            })
                .overrideTemplate(MovieDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MovieDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MovieService);
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
