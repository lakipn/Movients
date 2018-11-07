/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MovientsTestModule } from '../../../test.module';
import { CastUpdateComponent } from 'app/entities/cast/cast-update.component';
import { CastService } from 'app/entities/cast/cast.service';
import { Cast } from 'app/shared/model/cast.model';

describe('Component Tests', () => {
    describe('Cast Management Update Component', () => {
        let comp: CastUpdateComponent;
        let fixture: ComponentFixture<CastUpdateComponent>;
        let service: CastService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MovientsTestModule],
                declarations: [CastUpdateComponent]
            })
                .overrideTemplate(CastUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CastUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CastService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Cast(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cast = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Cast();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cast = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
